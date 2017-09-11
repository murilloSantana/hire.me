## Ferramentas utilizadas

1. Linguagem: Java
2. Framework: Spring boot, Materialize e Jquery
3. Banco de dados: H2 (Escolhido pela facilidade de implementação, não necessitando pré-requisitos de instalação).

## Algoritmo para encurtar URL

Para Encurta a URL original passada pelo client, foi usado o murmur3 (função hash não criptográfica) de 32 bits, para ter um alias de tamanho fixo não grande, essa escolha se deu pelo murmur3 ser a função mais nova de 32 bits suportada pela api guava (utilizada pela facilidade de implementação).

## Testes

Foram criados testes unitários para a verificação das funções das classes de service e controller. Para facilitar os testes foi criado um builder da classe url, além de terem sido utilizados o Junit juntamente com o MockMvc.

## Urls mais acessadas

Foi desenvolvido um método que realiza uma consulta no banco de dados de maneira decrescente limitando o resultado em 10. 

## Client

Dois clients foram criados para acessar a API, o primeiro foi codificado em uma classe executada através de um método main, que então realiza as requisições. A segunda solução foi uma interface construida com html, jquery e materialize.

## Acesso ao Endpoints

Exemplos de acesso aos Endpoints:

* Chamada sem alias (shorten)
GET http://localhost:8080/shortenurl?urlOriginal=http://bemobi.com.br

* Chamada com alias (shorten)
GET http://localhost:8080/shortenurl?urlOriginal=http://bemobi.com.br&alias=bemobi

* Chamada com alias que já existe (shorten)
GET http://localhost:8080/shortenurl?urlOriginal=http://github.com&alias=bemobi

* Chamada retrieve (retrieve)
GET http://localhost:8080/retrieveurl?urlOriginal=alias=bemobi

* Chamada urls mais acessadas
GET http://localhost:8080/mostAccessed

* Chamada para o client com interface
http://localhost:8080/encurtador
