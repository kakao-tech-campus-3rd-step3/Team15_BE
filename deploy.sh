#!/bin/bash

# 프로젝트 경로와 jar 파일 이름 설정
PROJECT_PATH="/home/ubuntu/Team15_BE"
JAR_NAME="Team15_BE-0.0.1-SNAPSHOT.jar" # 빌드 결과물 이름에 맞게 수정

echo ">>> Git Pull (develop branch)"
cd $PROJECT_PATH
git pull origin develop

echo ">>> Project Build"
./gradlew build

echo ">>> Find running application PID"
CURRENT_PID=$(pgrep -f $JAR_NAME)

if [ -z "$CURRENT_PID" ]; then
    echo ">>> No running application to stop."
else
    echo ">>> Stop running application (PID: $CURRENT_PID)"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo ">>> Deploy new application"
JAR_PATH="$PROJECT_PATH/build/libs/$JAR_NAME"
nohup java -jar $JAR_PATH > /dev/null 2>&1 &