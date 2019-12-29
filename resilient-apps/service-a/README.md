# Build
mvn clean package && docker build -t com.jcrochavera/service-a .

# RUN

docker rm -f service-a || true && docker run -d -p 8080:8080 -p 4848:4848 --name service-a com.jcrochavera/service-a 