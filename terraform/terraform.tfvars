# locations
resource_group_name = "psr"
location            = "East US"
app_name            = "chessbase-psr-project"
secret_key          = "<SECRET KEY>"

#POSTGRES
postgres_server_name = "postgres-chessbase"
postgres_administrator_login = "<LOGIN>"
postgres_administrator_password = "<PASSWORD>"
postgres_db_name = "chessbase"
postgresql_firewall_rule = "allow-azure-services"

#ACR REGISTRY
acr_registry_name = "chessbaseacr"

#CONTAINER ENV
container_env_name = "chessbase-container-env"

#BACKEND
container_app_backend_name = "chessbase-backend"

#FRONTEND
container_app_frontend_name = "chessbase-frontend"