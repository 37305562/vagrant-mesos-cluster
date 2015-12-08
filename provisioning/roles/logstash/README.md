#LogStash

##Что это?
Роль для установки образа LogStash

##Переменные

* `private_registry` - URL docker-репозиотрия (`docker.moscow.alfaintra.net`, по-умолчанию)            
* `logstash_service_name` - имя сервиса (`logstash`, по-умолчанию)                               
* `logstash_image_name` - имя docker-образа (`logstash`, по-умолчанию)    
* `logstash_config_dir` - путь к шаблонам (`/etc/logstash/conf.d`, по-умолчанию)
* `log_type` - маркер типа логов (для фильтрации в Kibana)
* `elastic_logs_master` - хост ES для сохранения логов

### Пример использования

```
- hosts: dc1_docker
  sudo: yes
  vars:
    log_type: thrift-api
    elastic_logs_master: 127.0.0.1
  roles:
    - logstash
  tags: logstash
```