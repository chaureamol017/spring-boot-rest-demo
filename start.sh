
docker-compose up -d

docker exec -it db mysql -u root -pRoot@123 -e -p 3306:3306 "GRANT ALL PRIVILEGES ON *.* TO 'demo'@'%';"

