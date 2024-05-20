.PHONY: clean

clean:
	rm -rf pgdata

run:
	docker-compose up
	cd ./yandex-music-service && uvicorn main:app --host 127.0.0.1 --port 8000
	cd ./frontend && npm run serve

stop:
	docker-compose down

restart: stop clean run