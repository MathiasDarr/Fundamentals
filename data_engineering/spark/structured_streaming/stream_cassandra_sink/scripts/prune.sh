#!/bin/bash
docker system prune -f
docker volume prune -f
rm -rf checkpoints/*