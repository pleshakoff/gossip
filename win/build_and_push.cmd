set version=%1
set service=%2

echo %service%
cd d:\jprojects\gossip\%service%\
cmd /C gradlew assemble
docker image build -t pleshakoff/gossip-%service%:%version% .
docker image push pleshakoff/gossip-%service%:%version%

cd d:\jprojects\gossip\win
