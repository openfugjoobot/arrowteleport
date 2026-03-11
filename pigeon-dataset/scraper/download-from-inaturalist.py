#!/usr/bin/env python3
"""
Download Pigeon Images from iNaturalist API
Usage: python3 download-from-inaturalist.py [output_dir] [count]

iNaturalist: https://api.inaturalist.org/v1/
"""

import requests
import os
import sys
import time
from pathlib import Path

def search_observations(count=150):
    """Search for Rock Dove (Columba livia) observations"""
    
    api_url = "https://api.inaturalist.org/v1/observations"
    params = {
        "taxon_name": "Columba livia",  # Rock Dove / City Pigeon
        "per_page": 200,
        "order": "desc",
        "order_by": "created_at",
        "only Photos": "true"  # Only observations with photos
    }
    
    print(f"🕊️ Searching iNaturalist for Rock Dove observations...")
    response = requests.get(api_url, params=params, headers={"User-Agent": "PigeonBot/1.0"})
    data = response.json()
    
    results = data["results"][:count]
    print(f"Found {len(results)} observations with photos")
    
    image_info = []
    for obs in results:
        obs_id = obs["id"]
        photos = obs.get("photos", [])
        if photos:
            for photo in photos:
                image_info.append({
                    "obs_id": obs_id,
                    "url": photo["url"],
                    "license": photo.get("license", "unknown")
                })
    
    return image_info

def download_images(image_info, output_dir):
    """Download images to output directory"""
    
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)
    
    downloaded = 0
    failed = 0
    
    for i, info in enumerate(image_info):
        ext = info["url"].split(".")[-1].split("?")[0]
        if ext not in ["jpg", "jpeg", "png"]:
            ext = "jpg"
        
        filename = f"pigeon_{i:03d}.{ext}"
        filepath = output_path / filename
        
        try:
            response = requests.get(info["url"], headers={"User-Agent": "Mozilla/5.0"}, timeout=10)
            if response.status_code == 200:
                with open(filepath, "wb") as f:
                    f.write(response.content)
                print(f"✓ [{i+1}] {filename} (License: {info['license']})")
                downloaded += 1
            else:
                print(f"✗ [{i+1}] HTTP {response.status_code}")
                failed += 1
        except Exception as e:
            print(f"✗ [{i+1}] Error: {e}")
            failed += 1
        
        time.sleep(0.5)  # Rate limiting
    
    return downloaded, failed

if __name__ == "__main__":
    output_dir = sys.argv[1] if len(sys.argv) > 1 else "/home/ubuntu/.openclaw/workspace/pigeon-dataset/images"
    count = int(sys.argv[2]) if len(sys.argv) > 2 else 150
    
    print(f"🕊️ iNaturalist Pigeon Downloader")
    print(f"Output: {output_dir}")
    print(f"Count: {count} images\n")
    
    image_info = search_observations(count)
    print(f"\nDownloading {len(image_info)} images...\n")
    
    downloaded, failed = download_images(image_info, output_dir)
    
    print(f"\n✅ Done!")
    print(f"Downloaded: {downloaded}")
    print(f"Failed: {failed}")
    print(f"Location: {output_dir}")
