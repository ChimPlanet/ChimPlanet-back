# run_new_was.sh

#!/bin/bash

CURRENT_PORT=$(cat /home/ec2-user/service_url.inc | grep -Po '[0-9]+' | tail -1)
JAR_NAME=$(ls /home/ec2-user/chimplanet/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=/home/ec2-user/chimplanet/build/libs/$JAR_NAME
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill ${TARGET_PID}
fi

# nohup java -jar -Dspring.profiles.active=prod -Dserver.port=${TARGET_PORT} $JAR_PATH > /dev/null 2> /dev/null < /dev/null & >
nohup java -jar -Dspring.profiles.active=prod -Dserver.port=${TARGET_PORT} $JAR_PATH > /dev/null 2> /dev/null < /dev/null   &
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0
