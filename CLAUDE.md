# Sidecar

On-device code review. A quantized LLM runs on an Android phone, reviews diffs pushed
from a laptop over USB, and explains bugs + writes patches — with the phone in airplane
mode. Nothing ever leaves the device.

Built for the **iQOO Hackathon 2026** (phone-first AI, India). Track: **Developer Tools**.

## Deadlines

- **Oct 5, 2026** — idea submission locks. This is the real deadline.
- **Oct 9-11, 2026** — Grand Finale, Bengaluru (only if shortlisted).

Submission deliverables: **prototype video + landing page**. Deck comes later.
There is **no live demo** for shortlisting.

## Judged on (published rubric)

| Weight | Criterion |
|---|---|
| 25% | iQOO Office Kit usage (phone-bridge time during the event) |
| 25% | Phone-first execution — runs natively on the iQOO 15 |
| 20% | AI-native build — model choice + mobile latency, not bolted on |
| 20% | Problem fit |
| 10% | Craft & pitch |

Every decision below traces back to this table. Half the score is "it runs on the phone."

## The product

```
Mac: git hook → staged diff
  ↓ USB (adb — works in airplane mode)
Phone: split into hunks
  → [post-submission] deterministic rule pass flags candidates
  → on-device LLM explains the bug + writes the patch   ← the AI work
  → free-text "ask about this diff"
  ↓ USB
Mac: [post-submission] tap to apply patch
```

**Critical design constraint:** a 1B model is bad at *finding* bugs in code. It is good at
explaining and patching a small, pre-localized hunk. Detection is meant to be deterministic.
For the Oct 5 video the bug is **planted in the demo diff**, so pure-LLM on one small hunk
is fine. Do not build the rule pass before Oct 5 — it does not show on camera.

## Stack — settled, do not re-litigate

- **Kotlin + Jetpack Compose.** No Flutter, no React Native. Owner is an iOS/Swift dev;
  Compose is the closest analog to SwiftUI.
- **MediaPipe LLM Inference** (`com.google.mediapipe:tasks-genai`). Not llama.cpp — NDK/JNI
  cross-compilation is not affordable on this timeline.
- **Gemma 3 1B IT, int4 `.task`** from `litert-community/Gemma3-1B-IT` on HuggingFace.
  Not Kaggle (raw weights, needs conversion).
- Model lives at `/data/local/tmp/llm/model.task`, pushed via adb. **Never bundled in the APK.**
- **Transport: adb over USB.** Venue wifi is always broken, and USB survives airplane mode —
  which is the entire demo. LAN is fallback only.
- **`LlmInference` is deprecated — ignore it.** The successor, `com.google.ai.edge.litertlm`,
  is at `0.0.0-alpha05`. Deprecated is not broken, and this is still the API Google's Android
  guide uses. Do not migrate before the finale.
- **No emulator, ever.** MediaPipe LLM Inference does not run on emulators.
- **No NPU / QNN before the finale.** Cannot be tested on the dev device. Day-one stretch goal.

## Design system — HARD CONSTRAINTS

The brief is: **must not look AI-generated.** That is a specific look, and it is banned.

**Banned, no exceptions:** Inter, Poppins, Montserrat · purple/blue gradients · any gradient
at all · glassmorphism · backdrop-blur · soft shadows · `border-radius` > 2px · Lucide,
Feather, Heroicons, or any icon library · ✨ or any emoji · centered hero with gradient
headline · stock Material 3 defaults (purple ripple, tonal buttons, 16dp corners).

**Direction: terminal brutalist.** A machine readout — dense, hard-edged, showing its internals.

Type:
- **Departure Mono** (departuremono.com, free) — UI, data, code, labels
- **Instrument Serif** (Google Fonts, free) — one big statement line per screen

Extreme scale contrast: 11px mono labels against 72px serif display.

Tokens:
```
paper  #EDEAE0   warm off-white ground (web/landing)
ink    #111110   near-black — never pure #000
acid   #D8FF3E   the one accent
blood  #FF3B1F   errors / the found bug
```
Phone app inverts: `#0C0C0B` ground, `#EDEAE0` text, same acid.

Rules:
- Icons are **unicode geometry and ASCII only**: `▲ ● ■ → ↳ ✕ ░ ▌`
- 1px solid rules, visible grid, left-aligned, asymmetric
- Telemetry as decoration — `18.4 tok/s · int4 · 0 bytes sent` — because it is true, and
  authentic detail is what fake design cannot fake
- Android: custom Compose theme written from scratch, fonts as `.ttf` in `res/font`,
  custom ripple. Do not theme Material 3 — replace it.

## Environment

- macOS, ~15GB free. **Watch disk usage.** No Xcode (not needed).
- Android Studio installed. Emulator/system images deliberately NOT installed.
- **Test device: a OnePlus (~₹40k, Snapdragon 8-series, 8GB+).** Borrowed, not always in hand.
  It is the only real test target. Confirm chipset with `adb shell getprop ro.soc.model`.
- Owner is an **iOS/Swift developer, new to Android.** Explain Android-specific concepts;
  Kotlin↔Swift analogies are useful. Claude writes all the code.

## Plan

| Day | | Work | Gate |
|---|---|---|---|
| D1 | Sep 30 | Android skeleton, MediaPipe wired, model on device | Model returns text on the OnePlus |
| D2 | Oct 1 | The one screen — diff view, streaming, tok/s counter, custom theme | Looks right on camera |
| D3 | Oct 2 | Git hook + adb push bridge | A real diff renders on the phone |
| D4 | Oct 3 | Record + edit video (80s) | Video done |
| D5 | Oct 4 | Landing page | Deployed |
| D6 | Oct 5 | Buffer + submit | Submitted |

If D1 slips a full day, drop the git hook and load the diff from a bundled sample file.
The video barely changes.

## Out of scope before Oct 5

Rule-pass detection · apply-patch round trip · the deck · NPU · settings, error states,
onboarding, any second screen · Office Kit integration as a product feature (it is an
event-day scoring mechanic, not a build task).

## Commands

```bash
./gradlew assembleDebug                  # build
adb install -r app/build/outputs/apk/debug/app-debug.apk
adb logcat -s Sidecar                    # app logs
adb shell getprop ro.soc.model           # confirm chipset
adb shell screenrecord /sdcard/demo.mp4  # clean capture, no overlay, 3min cap
```
