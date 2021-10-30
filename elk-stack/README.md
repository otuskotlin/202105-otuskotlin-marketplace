# Стенд для тестирования ELK-Stack

## docker-compose-light.yml

Содержит минимальный набор служб для работы с Кибаной. Содержит только ElasticSearch и Kibana.
База данных Эластика включена и находится в папке `esdata`.

Запуск:
```bash
docker-compose -f docker-compose-light.yml up
```

## docker-compose-full.yml

Содержит полный набор служб для отправки данных из программ в Эластик и отрисовки этих данных в
Кибане.

## ElasticSearch

Для запуска требуется запустить команды. Без них ElasticSearch при запуске падает
с ошибкой.
```bash
sudo sh -c "echo \"vm.max_map_count=262144\" > /etc/sysctl.d/10-elasticsearch.conf"
sudo sysctl -p
```
