# Icetrucks

This RESTful API allows you to manage your ice cream trucks and ice creams.

## Demo

You can access the public instance of this service over at https://icecreamtruck.lol

## Members

| Member | Role |
|--------------------------------------------------------------|------------------------|
| [Valentin Ricard](https://github.com/valentin-ricard)        | Writing code           |
| [Alexandre Philibert](https://github.com/AlexandrePhilibert) | Writing Code           |
| [Gwendal Piemontesi](https://github.com/gwendalpiemonte/)    | Deployment             |
| [Guillaume Trüeb](https://github.com/truebguillaume/)        | Secretary + Deployment |

## Deploy the app

If you are a more seasoned sys-admin, you can install Icecream truck Simulator with the following:

**Pre-requisites**:
- Login with an SSH key instead of the password. Here's a [digitalocean documentation page](https://www.digitalocean.com/community/tutorials/how-to-set-up-ssh-keys-on-debian-11) to help you
  
  TL;DR:
  ```shell
  # Generate on your local computer a SSH key:
  ssh-keygen -t ed25519
  # Copy the key to the remote server:
  ssh-copy-id heiguser@<machine_ip>
  # Disable SSH password authentication
  sudo nano /etc/ssh/sshd_config
  # In that file, replace 
  # 
  # PasswordAuthentication yes
  # to 
  # PasswordAuthentication no
  # 
  # Save, and quit.
  
  # Restart the ssh server to apply the changes
  sudo systemctl restart ssh
  ```
- You need a working installation of docker (see https://github.com/docker/docker-install for a convenience script)
  
  TL;DR:
  ```shell
  curl -fsSL https://get.docker.com -o get-docker.sh
  # Validate the script manually, to ensure it does not do things that break your server!
  sh get-docker.sh
  ```
- The port `443` **MUST** be accessible on the internet
  (the default compose provides a loadbalancer with automatic certificate issuance)
> [!NOTE]
> If you have a server provided by the HEIG following the template, this should aleady be done for you.
- Get your token to interact with the GHCR repository
  (see [Github's documentation](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry#authenticating-to-the-container-registry)
  to get started)
  
  TL;DR:
  - Go to [Github PAT's page](https://github.com/settings/tokens)
  - Create a new **classic token**
  - Grant it the `package:read` permission
  - Execute the following command on the server (replacing the values between `<>` with your values:
    ```shell
    export CR_PAT=<THE TOKEN YOU GOT FROM GITHUB>
    echo $CR_PAT | docker login ghcr.io -u <YOUR USERNAME> --password-stdin
    ```

> [!WARNING]
> We encourage you to only download the `docker-compose.prod.yml` instead of cloning the entire repository for the following reasons:
> - This encourages you to use the pre-built images, instead of building them yourself. The images are built automatically on commit.
>   - As a side effect, this makes automatic updates (using the remote registry) easier than local image builds.
> 
> If you still want to do it this way, here is the way to do it:
> ```shell
> git clone https://github.com/AlexandrePhilibert/dai-labo-4.git
> cd ./dai-labo-4
> rm ./docker-compose.yaml
> mv ./docker-compose.prod.yaml ./docker-compose.yaml
> # Replace everything within angle brackets with your values
> ${VISUAL:-${EDITOR:-vi}} "docker-compose.yaml"
> # Start the container stack
> docker compose up -d
> # Wait a minute for the certificates to be issued, and tada 🎉!
> ```

After that, you can download the prepared [docker-compose.yaml file](./docker-compose.yaml),
configure it to your liking, and profit 💸💸💸
```shell
wget https://github.com/AlexandrePhilibert/dai-labo-4/raw/main/docker-compose.prod.yaml
mv docker-compose.prod.yaml docker-compose.yaml
# Replace everything within angle brackets with your values
${VISUAL:-${EDITOR:-vi}} "docker-compose.yaml"
# Start the container stack
docker compose up -d
# Wait a minute for the certificates to be issued, and tada 🎉!
```

## Access the app

In order to access the API documentation, go to : `https://<your domain>/docs`.

For local development, the url is : `https://localhost:8080/docs`

## Configure the DNS zone

Create an A record for the API:

```
A <your domain> <the server ip>
```

Create an A record for the whoami service:

```
A whoami.<your domain> <the server ip>
```

## Docker

Build the docker image:

```sh
docker build -t ghcr.io/alexandrephilibert/dai-labo-4:latest
``` 

You can publish the image to ghcr using the following command:

```sh
docker push ghcr.io/alexandrephilibert/dai-labo-4:latest
```

You must have the `package:write` permissions in order to push the image.

Here is the [link](https://github.com/AlexandrePhilibert/dai-labo-4/pkgs/container/dai-labo-4) to the docker image.

## Examples

You can learn more about the design of the documentation on [USAGE.md](USAGE.md).

By default, the application also provides an interactive documentation at `<your server url>/docs`
