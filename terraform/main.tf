terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "3.104.2"
    }
  }
}

provider "azurerm" {
  skip_provider_registration = true
  features {}
}

resource "azurerm_resource_group" "rg" {
  name     = var.resource_group_name
  location = var.location
}

resource "azurerm_postgresql_server" "postgres" {
  name                = var.postgres_server_name
  location            = azurerm_resource_group.rg.location
  resource_group_name = azurerm_resource_group.rg.name

  sku_name = "B_Gen5_2"
  storage_mb                   = 5120
  backup_retention_days        = 7
  geo_redundant_backup_enabled = false
  auto_grow_enabled            = true

  administrator_login          = var.postgres_administrator_login
  administrator_login_password = var.postgres_administrator_password

  version = "11"
  ssl_enforcement_enabled      = true
  ssl_minimal_tls_version_enforced = "TLS1_2"
}

resource "azurerm_postgresql_database" "db" {
  name                = var.postgres_db_name
  resource_group_name = azurerm_resource_group.rg.name
  server_name         = azurerm_postgresql_server.postgres.name
  charset             = "UTF8"
  collation           = "English_United States.1252"
}

resource "azurerm_postgresql_firewall_rule" "allow_azure_services" {
  name                = var.postgresql_firewall_rule
  resource_group_name = azurerm_resource_group.rg.name
  server_name         = azurerm_postgresql_server.postgres.name
  start_ip_address    = "0.0.0.0"
  end_ip_address      = "0.0.0.0"
}

resource "azurerm_container_app_environment" "env" {
  name                = var.container_env_name
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
}

resource "azurerm_container_registry" "acr" {
  name                = var.acr_registry_name
  resource_group_name = azurerm_resource_group.rg.name
  location            = azurerm_resource_group.rg.location
  sku                 = "Basic"
  admin_enabled       = true
}

output "acr_login_server" {
  value = azurerm_container_registry.acr.login_server
}

output "acr_admin_username" {
  value = azurerm_container_registry.acr.admin_username
}

output "acr_admin_password" {
  value     = azurerm_container_registry.acr.admin_password
  sensitive = true
}


//backend

resource "azurerm_container_app" "backend_app" {
  name                         = var.container_app_backend_name
  resource_group_name          = azurerm_resource_group.rg.name
  container_app_environment_id = azurerm_container_app_environment.env.id
  revision_mode                = "Single"

  identity {
    type = "SystemAssigned"
  }

  secret {
    name = "password"
    value = azurerm_container_registry.acr.admin_password
  }
  secret {
    name = "secretkey"
    value = var.secret_key
  }

  registry {
    server                = azurerm_container_registry.acr.login_server
    username              = azurerm_container_registry.acr.admin_username
    password_secret_name  = "password"
  }

  ingress {
    external_enabled = true
    target_port      = 9000
    traffic_weight {
      latest_revision = true
      percentage      = 100
    }
  }

  template {
    container {
      name   = "backend"
      image  = "${azurerm_container_registry.acr.login_server}/chessbase-backend:latest"
      cpu    = 1
      memory = "2Gi"

      env {
        name  = "POSTGRES_PORT"
        value = "5432"
      }
      env {
        name  = "POSTGRES_URL"
        value = azurerm_postgresql_server.postgres.fqdn
      }
      env {
        name  = "POSTGRES_USER"
        value = "${azurerm_postgresql_server.postgres.administrator_login}@${azurerm_postgresql_server.postgres.name}"
      }
      env {
        name  = "POSTGRES_PASSWORD"
        value = azurerm_postgresql_server.postgres.administrator_login_password
      }
      env {
        name  = "POSTGRES_DB"
        value = azurerm_postgresql_database.db.name
      }
      env {
        name  = "PORT"
        value = "9000"
      }
      env {
        name = "SECRET_KEY"
        secret_name = "secretkey"
      }
    }

    min_replicas = 1
    max_replicas = 1
  }
}

output "backend_fqdn" {
  value = azurerm_container_app.backend_app.latest_revision_fqdn
}

resource "azurerm_role_assignment" "acr_pull_backend" {
  scope                = azurerm_container_registry.acr.id
  role_definition_name = "AcrPull"
  principal_id         = azurerm_container_app.backend_app.identity[0].principal_id
}


//frontend

resource "azurerm_container_app" "frontend_app" {
  name                         = var.container_app_frontend_name
  resource_group_name          = azurerm_resource_group.rg.name
  container_app_environment_id = azurerm_container_app_environment.env.id
  revision_mode                = "Single"

  identity {
    type = "SystemAssigned"
  }

  secret {
    name = "password"
    value = azurerm_container_registry.acr.admin_password
  }

  registry {
    server                = azurerm_container_registry.acr.login_server
    username              = azurerm_container_registry.acr.admin_username
    password_secret_name  = "password"
  }

  ingress {
    external_enabled = true
    target_port      = 3000
    traffic_weight {
      latest_revision = true
      percentage      = 100
    }
  }

  template {
    container {
      name   = "frontend"
      image  = "${azurerm_container_registry.acr.login_server}/chessbase-frontend:latest"
      cpu    = 1
      memory = "2Gi"

      env {
        name  = "REACT_APP_API_URL"
        value = "https://${azurerm_container_app.backend_app.latest_revision_fqdn}"
      }
    }

    min_replicas = 1
    max_replicas = 1
  }
}

output "frontend_fqdn" {
  value = azurerm_container_app.frontend_app.latest_revision_fqdn
}

resource "azurerm_role_assignment" "acr_pull_frontend" {
  scope                = azurerm_container_registry.acr.id
  role_definition_name = "AcrPull"
  principal_id         = azurerm_container_app.frontend_app.identity[0].principal_id
}