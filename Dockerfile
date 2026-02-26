FROM eclipse-temurin:11-jre-focal
RUN apt-get update && apt-get install -y curl unzip zip && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY sm-shop/target/shopizer.jar app.jar
# Override database.properties inside the jar to point to Docker MySQL
RUN printf 'db.jdbcUrl=jdbc\\:mysql\\://db\\:3306/SALESMANAGER?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8\n\
db.user=shopizer\n\
db.password=very-long-shopizer-password\n\
db.driverClass=com.mysql.cj.jdbc.Driver\n\
hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect\n\
db.show.sql=false\n\
db.preferredTestQuery=SELECT 1\n\
db.schema=SALESMANAGER\n\
hibernate.hbm2ddl.auto=update\n\
db.initialPoolSize=4\n\
db.minPoolSize=4\n\
db.maxPoolSize=8\n' > /tmp/database.properties \
  && cd /tmp && mkdir -p BOOT-INF/classes \
  && cp database.properties BOOT-INF/classes/ \
  && zip /app/app.jar BOOT-INF/classes/database.properties
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--config.cms.staticContentFilePath=/app/files"]
