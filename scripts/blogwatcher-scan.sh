#!/bin/bash
# Blogwatcher hourly scan - outputs new articles for OpenClaw

blogwatcher scan 2>&1 | grep -E "^Found [0-9]+ new article"