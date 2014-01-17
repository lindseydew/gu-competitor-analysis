#!/bin/bash
curl -H "Content-Type: application/json" --data @guardian-morning.json http://localhost:9000/insert/guardian
curl -H "Content-Type: application/json" --data @nytimes-morning.json http://localhost:9000/insert/nytimes
curl -H "Content-Type: application/json" --data @mailonline-morning.json http://localhost:9000/insert/mailonline
