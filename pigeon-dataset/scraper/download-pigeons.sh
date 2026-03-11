#!/bin/bash
# Pigeon Image Scraper - Downloads pigeon photos from Wikimedia Commons using MediaWiki API

set -e

OUTPUT_DIR="${1:-/home/ubuntu/.openclaw/workspace/pigeon-dataset/images}"
COUNT="${2:-150}"

echo "🕊️ Pigeon Image Scraper"
echo "Output: $OUTPUT_DIR"
echo "Count: $COUNT images"

# Create output directory
mkdir -p "$OUTPUT_DIR"
cd "$OUTPUT_DIR"

# Use MediaWiki API to get images from category
echo "Fetching images from Wikimedia Commons API..."

# API call to get category members (files in Columba livia category)
api_url="https://commons.wikimedia.org/w/api.php"
params="action=query\
&list=categorymembers\
&cmtitle=Category:Columba_livia\
&cmlimit=500\
&cmmstype=file\
&format=json"

echo "Querying API..."
curl -s "$api_url?$params" | \
  python3 -c "
import json, sys
data = json.load(sys.stdin)
files = data['query']['categorymembers']
for f in files[:$COUNT]:
    title = f['title'].replace(' ', '_')
    print(f'https://commons.wikimedia.org/wiki/{title}')
" > image_links.txt

echo "Found $(wc -l < image_links.txt) images"

# Now extract actual file URLs
echo "Resolving file URLs..."
for link in $(cat image_links.txt); do
  # Extract filename from URL
  filename=$(echo "$link" | sed 's/.*File://g' | sed 's/_/ /g')
  
  # Use API to get file info
  file_info=$(curl -s "$api_url?action=query\&titles=File:$filename\&prop=imageinfo\&iiprop=url\&format=json" 2>/dev/null)
  
  # Extract download URL
  download_url=$(echo "$file_info" | python3 -c "
import json, sys
try:
    data = json.load(sys.stdin)
    pages = data['query']['pages']
    for page in pages.values():
        if 'imageinfo' in page:
            print(page['imageinfo'][0]['url'])
            break
except: pass
" 2>/dev/null)

  if [ -n "$download_url" ]; then
    ext="${download_url##*.}"
    wget -q --user-agent="Mozilla/5.0" -O "pigeon_${RANDOM}.$ext" "$download_url" 2>/dev/null && echo "✓ Downloaded" || echo "✗ Failed"
  fi
  
  # Rate limiting
  sleep 0.3
done

echo "✅ Done! Images saved to $OUTPUT_DIR"
ls -la | grep -c "pigeon" || echo "No images downloaded"

# Cleanup
rm -f image_links.txt
