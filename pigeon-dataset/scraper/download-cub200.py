#!/usr/bin/env python3
"""
Download CUB-200-2011 Dataset - Caltech-UCSD Birds
Focus: Rock Dove (Species #152)
URL: https://data.caltech.edu/records/20098

This downloads the entire dataset (1.1 GB, 11,788 images)
Extracts only Rock Dove images (~60-80 images)
"""

import requests
import tarfile
import os
import sys
from pathlib import Path

CUB_URL = "https://data.caltech.edu/records/20098/files/CUB_200_2011.tgz"
CUB_DIR = "/home/ubuntu/.openclaw/workspace/pigeon-dataset/cub200"
OUTPUT_DIR = "/home/ubuntu/.openclaw/workspace/pigeon-dataset/images-highres"

def check_if_exists():
    """Check if dataset already downloaded"""
    if os.path.exists(CUB_DIR) and os.listdir(CUB_DIR):
        print(f"✓ CUB-200 already exists at {CUB_DIR}")
        return True
    return False

def download_and_extract():
    """Download and extract CUB-200 dataset"""
    
    print("🕊️ CUB-200-2011 Downloader")
    print(f"URL: {CUB_URL}")
    print(f"Size: ~1.1 GB")
    print()
    
    # Check if exists
    if check_if_exists():
        print("Skipping download, extracting Rock Dove images...")
        extract_rock_doves()
        return
    
    # Create directories
    Path(CUB_DIR).mkdir(parents=True, exist_ok=True)
    Path(OUTPUT_DIR).mkdir(parents=True, exist_ok=True)
    
    # Download
    tgz_path = os.path.join(CUB_DIR, "CUB_200_2011.tgz")
    
    if not os.path.exists(tgz_path):
        print("Downloading (this takes 5-10 min)...")
        
        response = requests.get(CUB_URL, stream=True, headers={"User-Agent": "PigeonBot/1.0"})
        total_size = int(response.headers.get('content-length', 0))
        downloaded = 0
        
        with open(tgz_path, 'wb') as f:
            for chunk in response.iter_content(chunk_size=8192):
                f.write(chunk)
                downloaded += len(chunk)
                if total_size:
                    percent = (downloaded / total_size) * 100
                    print(f"  Progress: {percent:.1f}%", end='\r')
        
        print(f"\n✓ Downloaded: {tgz_path} ({downloaded / 1024 / 1024:.1f} MB)")
    
    # Extract
    print("Extracting (2-3 min)...")
    with tarfile.open(tgz_path, 'r:gz') as tar:
        tar.extractall(CUB_DIR)
    
    print(f"✓ Extracted to {CUB_DIR}")
    
    # Extract Rock Doves
    extract_rock_doves()

def extract_rock_doves():
    """Extract only Rock Dove (Species #152) images"""
    
    # Rock Dove is species #152 in CUB-200
    # Path: CUB_200_2011/images/152.Lausanne_Swift/
    
    rock_dove_dir = os.path.join(CUB_DIR, "CUB_200_2011", "images", "152.Lausanne_Swift")
    
    if not os.path.exists(rock_dove_dir):
        print(f"✗ Rock Dove folder not found: {rock_dove_dir}")
        return
    
    import shutil
    
    count = 0
    for filename in os.listdir(rock_dove_dir):
        if filename.endswith(".jpg"):
            src = os.path.join(rock_dove_dir, filename)
            dst = os.path.join(OUTPUT_DIR, f"rocdo_{count:03d}.jpg")
            shutil.copy2(src, dst)
            count += 1
    
    print(f"\n✅ Rock Dove images copied: {count}")
    print(f"Location: {OUTPUT_DIR}")

if __name__ == "__main__":
    download_and_extract()
