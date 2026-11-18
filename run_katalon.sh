#!/bin/bash
# runs the suite through the offline Katalon Runtime Engine on this machine
# (the katalon wrapper in ~/.local/bin starts Studio's console mode) with
# Chrome 128 + chromedriver 128, one JVM at a time, log ~/katalon_cli_runs/
cd "$(dirname "$0")"

SUITE="${1:-Test Suites/web/web_smoke}"
PROFILE="${PROFILE:-default}"
mkdir -p ~/katalon_cli_runs
TS=$(date +%Y-%m-%d_%H-%M-%S)
LOG="$HOME/katalon_cli_runs/web-$TS.log"

echo "running $SUITE -> $LOG"
# optional live progress watcher, point KATALON_WATCH at any script that takes the log path
WPID=""
if [ -x "${KATALON_WATCH:-}" ]; then "$KATALON_WATCH" "$LOG" & WPID=$!; fi
"${KATALON_BIN:-$HOME/.local/bin/katalon}" "$(pwd)" \
    -testSuitePath="$SUITE" \
    -executionProfile="$PROFILE" \
    -browserType=Chrome \
    -retry=0 -data ~/.katalon_console/ws-web > "$LOG" 2>&1
RC=$?
[ -n "$WPID" ] && kill $WPID 2>/dev/null

echo "--- summary ---"
grep -E "Test Suites:|Test Cases:|FAILED|ERROR" "$LOG" | tail -8
if [ $RC -eq 0 ]; then
    echo "GREEN: katalonc exited 0, report in Reports/"
else
    echo "RED: katalonc exit $RC, full log: $LOG"
fi
exit $RC
