#!/usr/bin/env python3
"""
Download high-resolution pigeon images from Unsplash
Uses Unsplash API (no auth required for public search)

Endpoint: https://source.unsplash.com/ (deprecated)
Better: https://unsplash.com/s/photos/pigeon

Alternative: Pexels API (free, no auth for basic)
"""

import requests
import os
import sys
from pathlib import Path

# Using Pexels API (free tier, no key needed for basic search via web)
PEXELS_SEARCH = "https://www.pexels.com/search/pigeon/"

def download_from_pexels(count=50):
    """
    Download pigeon images from Pexels
    High-resolution, whole-body shots
    """
    
    output_dir = "/home/ubuntu/.openclaw/workspace/pigeon-dataset/images-highres"
    Path(output_dir).mkdir(parents=True, exist_ok=True)
    
    print("🕊️ Pexels Pigeon Downloader")
    print(f"Target: {count} high-res images")
    print()
    
    # Pexels doesn't have official public API without key
    # Using direct image URLs from curated list
    
    # Curated high-res pigeon images from Pexels (CC0 license)
    # These are direct image URLs
    image_urls = [
        "https://images.pexels.com/photos/...", # Would need actual URLs
    ]
    
    # Alternative: Use direct search
    # This requires web scraping which has bot protection
    
    print("Note: Pexels requires API key for programmatic access")
    print("Alternative: Unsplash API (free, requires key)")
    print()
    print("Options:")
    print("1. Get free Unsplash API key: https://unsplash.com/developers")
    print("2. Manual download from pexels.com/search/pigeon")
    print("3. Use CUB-200 dataset (scientific, high quality)")
    
    return 0

if __name__ == "__main__":
    download_from_pexels()
