FROM clojure:openjdk-11-tools-deps-1.10.3.855-slim-buster
MAINTAINER dongxu@apache.org
WORKDIR /app
COPY result.jar result.jar
RUN echo "grant { permission java.security.AllPermission; };" > ~/.java.policy
ENTRYPOINT ["java","-jar","/app/result.jar"]
EXPOSE 4000
