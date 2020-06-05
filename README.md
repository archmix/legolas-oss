### Docker setup
Legolas uses docker containers in order to provide starters. Because of this you need to configure docker to be accessible from your linux user. To enable this, please proceed with the steps below.

1. Add your linux user to the docker group
`sudo usermod -aG docker $(whoami)`
2. Restart your machine