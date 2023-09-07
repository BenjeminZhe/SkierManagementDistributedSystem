# SkierManagementDistributedSystem
## Introduction:
A distributed, concurrent skier resort management system that utilizes messaging brokers to process visitor records for skier resorts.

The HTTP web servers(Tomcat Server) were hosted on AWS EC2 to handle authentication and create visiting records.

The multi-threaded Java client was also hosted in EC2s for testing based on metrics such as latencies, throughputs, and P99 response time. Some optimization strategies used to improve overall system performance are AWS Load Balancing and RabbitMQ based Messaging Architecture.

Used Redis as cache and data storage.

## Tech stacks:
Tomcat, Load balancer, RabbitMQ, Redis, JMeter
