<h1 align="center">Algafood - API</h1>


## O que é

É uma implementação simplificada do IFOOD. algumas das operações possiveis são criar usuario, associar à grupos, à permissões ter diferentes niveis de acessos entre outros.

## Necessario para rodar o Projeto
- docker compose
- docker


## Começando

```
# clone o projeto 
git clone https://github.com/DeykyPinheiro/algafood


# rodar o api
docker compose up
```




## Tecnologias utilizadas
- Spring 3
- Java 17
- Mysql 8.0
- Redis 6.2.1
- Junit

## Rodar testes
```
# derruba todos os container em execucao
docker rm $(docker ps -aq) --force

# sobe o redis e o mysql
docker run -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql:8.0
docker run -d -p 6379:6379 --name  algafood-redis redis:6.2.1-alpine

#builda o projeto e roda os testes
./mvnw clean package
```

## Documentacao
```
# Acesse
localhost:8080/swagger-ui/index.html
```

caso queira visualizar no postman basta importar os dois arquivos dentro da pasta "documentacao-algafood"

## Autor

Desenvolvedor Java Jr

[![LinkdIn Badge](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=whit)](https://www.linkedin.com/in/deyky-pinheiro-bbb735125/)
