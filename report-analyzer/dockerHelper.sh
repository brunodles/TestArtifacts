#!/bin/bash
#readonly IMAGE_NAME=node_react
readonly IMAGE_NAME=node:9.4
#readonly CONTAINER_NAME=${IMAGE_NAME}_${PWD##*/}
readonly CONTAINER_NAME=testArtifactsAnalyzer
readonly PORTS=3000:3000

case $1 in
  create)
    echo "Experiment using default node image"
  ;;
  create2)
    docker image build -t $IMAGE_NAME . -f-<<EOF
FROM node:9.4

USER node
RUN mkdir /home/node/.npm-global
ENV PATH=/home/node/.npm-global/bin:$PATH
ENV NPM_CONFIG_PREFIX=/home/node/.npm-global

RUN npm install -g create-react-app
USER root
EOF
  ;;
  bash)
    (
      docker run -it -v "$(pwd):/home/node/app" -w "/home/node/app" --user=node -p $PORTS --name $CONTAINER_NAME $IMAGE_NAME bash
    ) || (
      echo "Did it failed? Try \"$0 restart\""
    )
  ;;
  restart)
    echo "Restarting"
    docker restart $CONTAINER_NAME
    $0 exec
  ;;
  exec)
    (
      docker exec -it $CONTAINER_NAME bash
    ) || (
      echo "Did it failed? Try \"$0 bash\""
    )
  ;;
  stop)
    echo "Stopping"
    docker stop --time 30 $CONTAINER_NAME
  ;;
  kill)
    echo "Killing"
    docker stop $CONTAINER_NAME
  ;;
  rm)
    echo "Removing container"
    docker rm $CONTAINER_NAME
  ;;
  vars)
    echo IMAGE_NAME=$IMAGE_NAME
    echo CONTAINER_NAME=$CONTAINER_NAME
  ;;
  help|--help|-h)
    cat <<-TEXT
Docker Helper
Useful scripts to work with docker.

Usage $0 [option]

Options:
  create    Create the node react image
  bash      Start image with bash
  restart   Restart container
  exec      Connect to a running container
  stop      Stop running container
  kill      Kill running container
  rm        Remove container
  vars      Show vars
  help      Show this help
TEXT
    ;;
    *)
      echo "Option \"$1\" not found!"
      $0 help
    ;;
esac
