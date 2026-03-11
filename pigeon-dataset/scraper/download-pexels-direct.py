#!/usr/bin/env python3
"""
Download high-resolution pigeon images from Pexels
Uses direct CDN URLs (no API key required)

Pexels CDN format:
https://images.pexels.com/photos/{id}/pexels-photo-{id}.jpeg

Curated list of pigeon photo IDs from pexels.com
"""

import requests
import os
from pathlib import Path
import time

# Curated Pexels pigeon photo IDs (high-res, whole body)
# Sourced from: https://www.pexels.com/search/pigeon/
PEXELS_PHOTO_IDS = [
    # High-res pigeon images (whole body, good quality)
    3225650,  # Pigeon on pavement
    2474593,  # Pigeon close-up
    1543767,  # Pigeon walking
    2614020,  # Pigeon portrait
    3225664,  # Pigeons together
    2249970,  # Pigeon head turn
    2614051,  # Pigeon side view
    3225541,  # Pigeon full body
    1543792,  # Pigeon standing
    2614028,  # Pigeon profile
    2474600,  # Pigeon feathers
    3225518,  # Pigeon details
    2614032,  # Pigeon eye detail
    1543743,  # Pigeon wing
    3225579,  # Pigeon walking away
    2249988,  # Pigeon looking back
    2614038,  # Pigeon feeding
    3225605,  # Pigeon on wall
    1543780,  # Pigeon on ground
    2614045,  # Pigeon in flight
    # Add more IDs as needed
]

def download_images(photo_ids, output_dir, count=50):
    """Download images from Pexels CDN"""
    
    output_path = Path(output_dir)
    output_path.mkdir(parents=True, exist_ok=True)
    
    print(f"🕊️ Pexels Pigeon Downloader")
    print(f"Output: {output_dir}")
    print(f"Count: {min(len(photo_ids), count)} images")
    print()
    
    base_url = "https://images.pexels.com/photos/{id}/pexels-photo-{id}.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
    
    downloaded = 0
    failed = 0
    
    for i, photo_id in enumerate(photo_ids[:count]):
        url = base_url.format(id=photo_id)
        filename = f"pigeon_pexels_{photo_id}.jpg"
        filepath = output_path / filename
        
        try:
            response = requests.get(url, headers={"User-Agent": "Mozilla/5.0"}, timeout=10)
            if response.status_code == 200:
                with open(filepath, "wb") as f:
                    f.write(response.content)
                # Check file size
                size = os.path.getsize(filepath) / 1024  # KB
                print(f"✓ [{i+1}] {filename} ({size:.0f} KB)")
                downloaded += 1
            else:
                print(f"✗ [{i+1}] HTTP {response.status_code}")
                failed += 1
        except Exception as e:
            print(f"✗ [{i+1}] Error: {e}")
            failed += 1
        
        # Rate limiting - be gentle
        time.sleep(0.5)
    
    print(f"\n✅ Done!")
    print(f"Downloaded: {downloaded}")
    print(f"Failed: {failed}")
    print(f"Location: {output_dir}")
    
    return downloaded

if __name__ == "__main__":
    output_dir = "/home/ubuntu/.openclaw/workspace/pigeon-dataset/images-highres"
    download_images(PEXELS_PHOTO_IDS, output_dir, count=50)
