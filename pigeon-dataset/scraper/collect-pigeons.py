#!/usr/bin/env python3
"""
Pigeon Image Collector - Downloads city pigeon photos from Wikimedia Commons
Usage: python3 collect-pigeons.py [output_dir] [count]
"""

import requests
import os
import sys
import time
import random
from pathlib import Path

def get_pigeon_images(count=150):
    """Fetch image URLs from Wikimedia Commons API"""
    
    api_url = "https://commons.wikimedia.org/w/api.php"
    params = {
        "action": "query",
        "list": "categorymembers",
        "cmtitle": "Category:Columba_livia",
        "cmlimit": 500,
        "cmmstype": "file",
        "format": "json"
    }
    
    print(f"🕊️ Fetching pigeon images from Wikimedia Commons...")
    response = requests.get(api_url, params=params, headers={"User-Agent": "PigeonBot/1.0"})
    data = response.json()
    
    files = data["query"]["categorymembers"][:count]
    print(f"Found {len(files)} images in category")
    
    image_urls = []
    for f in files:
        # Get file URL via another API call
        file_params = {
            "action": "query",
            "titles": f["title"],
            "prop": "imageinfo",
            "iiprop": "url",
            "format": "json"
        }
        
        file_response = requests.get(api_url, params=file_params, headers={"User-Agent": "PigeonBot/1.0"})
        file_data = file_response.json()
        
        pages = file_data["query"]["pages"]
        for page in pages.values():
            if "imageinfo" in page:
                url = page["imageinfo"][0]["url"]
                image_urls.append(url)
                break
        
        time.sleep(0.2)  # Rate limiting
    
    return image_urls

def download_images(urls, output_dir):
    """Download images to output directory"""
    
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)
    
    downloaded = 0
    failed = 0
    
    for i, url in enumerate(urls):
        ext = url.split(".")[-1].split("?")[0]
        if ext not in ["jpg", "jpeg", "png"]:
            ext = "jpg"
        
        filename = f"pigeon_{i:03d}.{ext}"
        filepath = output_path / filename
        
        try:
            response = requests.get(url, headers={"User-Agent": "Mozilla/5.0"}, timeout=10)
            if response.status_code == 200:
                with open(filepath, "wb") as f:
                    f.write(response.content)
                print(f"✓ [{i+1}] {filename}")
                downloaded += 1
            elif response.status_code == 429:
                print(f"⚠ [{i+1}] Rate limited, waiting 5s...")
                time.sleep(5)
                # Retry once
                response = requests.get(url, headers={"User-Agent": "Mozilla/5.0"}, timeout=10)
                if response.status_code == 200:
                    with open(filepath, "wb") as f:
                        f.write(response.content)
                    print(f"✓ [{i+1}] {filename} (retry)")
                    downloaded += 1
                else:
                    failed += 1
            else:
                print(f"✗ [{i+1}] HTTP {response.status_code}")
                failed += 1
        except Exception as e:
            print(f"✗ [{i+1}] Error: {e}")
            failed += 1
        
        time.sleep(2.0)  # Rate limiting - slower to avoid 429
    
    return downloaded, failed

if __name__ == "__main__":
    output_dir = sys.argv[1] if len(sys.argv) > 1 else "/home/ubuntu/.openclaw/workspace/pigeon-dataset/images"
    count = int(sys.argv[2]) if len(sys.argv) > 2 else 150
    
    print(f"🕊️ Pigeon Image Collector")
    print(f"Output: {output_dir}")
    print(f"Count: {count} images\n")
    
    urls = get_pigeon_images(count)
    print(f"\nDownloading {len(urls)} images...\n")
    
    downloaded, failed = download_images(urls, output_dir)
    
    print(f"\n✅ Done!")
    print(f"Downloaded: {downloaded}")
    print(f"Failed: {failed}")
    print(f"Location: {output_dir}")
