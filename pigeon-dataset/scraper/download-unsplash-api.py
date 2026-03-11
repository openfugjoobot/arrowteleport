#!/usr/bin/env python3
"""
Download high-resolution pigeon images from Unsplash API
Requires: UNSPLASH_ACCESS_KEY in .env or environment

API Docs: https://unsplash.com/documentation
Rate Limit: ~50 requests/hour (free tier)
"""

import requests
import os
import sys
from pathlib import Path
from dotenv import load_dotenv

# Load environment variables
load_dotenv(Path.home() / ".openclaw" / ".env")

UNSPLASH_ACCESS_KEY = os.getenv("UNSPLASH_ACCESS_KEY")

def search_and_download(keyword="pigeon", count=50, orientation="landscape"):
    """Search Unsplash and download images"""
    
    if not UNSPLASH_ACCESS_KEY:
        print("❌ UNSPLASH_ACCESS_KEY not found!")
        print("Get free key: https://unsplash.com/developers")
        print("Add to ~/.openclaw/.env as: UNSPLASH_ACCESS_KEY=your_key")
        return 0
    
    output_dir = "/home/ubuntu/.openclaw/workspace/pigeon-dataset/images-highres"
    Path(output_dir).mkdir(parents=True, exist_ok=True)
    
    print(f"🕊️ Unsplash Pigeon Downloader")
    print(f"Keyword: {keyword}")
    print(f"Count: {count} images")
    print(f"Orientation: {orientation}")
    print()
    
    # Search API
    search_url = "https://api.unsplash.com/search/photos"
    params = {
        "query": keyword,
        "per_page": min(count, 30),  # Max 30 per page
        "orientation": orientation,
        "order_by": "relevant"
    }
    headers = {
        "Authorization": f"Client-ID {UNSPLASH_ACCESS_KEY}"
    }
    
    print("Searching Unsplash...")
    response = requests.get(search_url, params=params, headers=headers)
    
    if response.status_code == 401:
        print("❌ Invalid API key")
        return 0
    elif response.status_code == 403:
        print("❌ API key rate limited or invalid")
        return 0
    
    data = response.json()
    results = data["results"]
    print(f"Found {len(results)} photos")
    print()
    
    downloaded = 0
    for i, photo in enumerate(results):
        # Get high-res download URL
        photo_id = photo["id"]
        download_url = f"https://api.unsplash.com/photos/{photo_id}/download"
        
        # Get actual image URL (requires second API call)
        img_url = photo["urls"]["regular"]  # 1080x... good enough
        
        # Download
        ext = "jpg"
        filename = f"pigeon_unsplash_{i:03d}.{ext}"
        filepath = output_dir / filename
        
        try:
            img_response = requests.get(img_url, headers=headers, timeout=10)
            if img_response.status_code == 200:
                with open(filepath, "wb") as f:
                    f.write(img_response.content)
                print(f"✓ [{i+1}] {filename}")
                downloaded += 1
            else:
                print(f"✗ [{i+1}] HTTP {img_response.status_code}")
        except Exception as e:
            print(f"✗ [{i+1}] Error: {e}")
        
        # Rate limiting
        import time
        time.sleep(0.5)
    
    print(f"\n✅ Done!")
    print(f"Downloaded: {downloaded}")
    print(f"Location: {output_dir}")
    
    return downloaded

if __name__ == "__main__":
    keyword = sys.argv[1] if len(sys.argv) > 1 else "pigeon"
    count = int(sys.argv[2]) if len(sys.argv) > 2 else 50
    orientation = sys.argv[3] if len(sys.argv) > 3 else "all"
    
    search_and_download(keyword, count, orientation)
