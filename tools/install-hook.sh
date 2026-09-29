#!/usr/bin/env bash
# Installs push-diff.sh as a pre-commit hook in the repo you want reviewed.
#   ./tools/install-hook.sh ~/code/some-repo
set -euo pipefail

TARGET="${1:-}"
if [ -z "$TARGET" ] || [ ! -d "$TARGET/.git" ]; then
  echo "usage: $0 /path/to/a/git/repo" >&2
  exit 1
fi

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
HOOK="$TARGET/.git/hooks/pre-commit"

cat > "$HOOK" <<EOF
#!/usr/bin/env bash
exec "$HERE/push-diff.sh"
EOF
chmod +x "$HOOK"

echo "sidecar: hook installed at $HOOK"
