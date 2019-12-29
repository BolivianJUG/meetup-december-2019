# Build
mvn clean package && docker build -t com.jcrochavera/service-b .

# RUN

docker rm -f service-b || true && docker run -d -p 8080:8080 -p 4848:4848 --name service-b com.jcrochavera/service-b 