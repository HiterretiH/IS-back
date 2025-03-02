# Use an official OpenJDK image as a parent image
FROM openjdk:17-jdk

# Set environment variables for WildFly
ENV WILDFLY_HOME /opt/wildfly
ENV WILDFLY_VERSION 30.0.0.Final

# Create the directory for WildFly installation
RUN mkdir -p $WILDFLY_HOME

# Remove any existing deployments with the same name to avoid conflicts
RUN rm -f $WILDFLY_HOME/standalone/deployments/info-systems-back-1.0.war
RUN rm -f $WILDFLY_HOME/standalone/deployments/*.dodeploy $WILDFLY_HOME/standalone/deployments/*.undeploy

# Copy WildFly binaries and configuration files
COPY wildfly-course / $WILDFLY_HOME

# Copy the WAR file to the WildFly deployments directory
COPY target/info-systems-back-1.0.war $WILDFLY_HOME/standalone/deployments/

# Expose WildFly HTTP and management ports
EXPOSE 8080 9990

# Set the working directory to WildFly
WORKDIR $WILDFLY_HOME

# Command to start WildFly server
CMD ["bin/standalone.sh", "-b", "0.0.0.0"]

