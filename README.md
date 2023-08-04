clean:

mvn clean package

build:

mvn -DskipTests=true -Pnative -Dagent package