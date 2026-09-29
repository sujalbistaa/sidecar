#!/usr/bin/env bash
# Sends the staged diff to the phone over USB.
#
# No network involved: adb runs over the USB cable, which is why the whole
# thing keeps working with the phone in airplane mode.
set -euo pipefail

ADB="${ADB:-$HOME/Library/Android/sdk/platform-tools/adb}"
REMOTE_DIR=/data/local/tmp/llm
REMOTE_FILE="$REMOTE_DIR/current.diff"

if [ ! -x "$ADB" ]; then
  echo "sidecar: adb not found at $ADB (set ADB=/path/to/adb)" >&2
  exit 1
fi

if ! "$ADB" get-state >/dev/null 2>&1; then
  echo "sidecar: no device. Check the cable and that USB debugging is on." >&2
  exit 1
fi

TMP="$(mktemp -t sidecar)"
trap 'rm -f "$TMP"' EXIT

git diff --cached -U3 > "$TMP"

if [ ! -s "$TMP" ]; then
  echo "sidecar: nothing staged, skipping." >&2
  exit 0
fi

"$ADB" shell mkdir -p "$REMOTE_DIR"
"$ADB" push "$TMP" "$REMOTE_FILE" >/dev/null
"$ADB" shell chmod 644 "$REMOTE_FILE"

echo "sidecar: pushed $(wc -l < "$TMP" | tr -d ' ') diff lines to the phone."
