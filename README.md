# PaisaFlow

**Voice-first AI Business Companion for small and rural/semi-urban entrepreneurs.**

> Simple outside. Sophisticated inside. · Your business. Understood.

Core loop: **TALK → UNDERSTAND → MODEL → PREDICT → SIMULATE → EXPLAIN → ACT → LEARN**

This repository contains the PaisaFlow Android app (Kotlin · Jetpack Compose) and its FastAPI backend.

**Screens:** Splash → Language → Onboarding → Home (Hindi v2 / Hinglish v1) · Voice Conversation (live mic via Bhashini) · Business Digital Twin + Health · What-If Simulator (live finance engine) · Evidence Mode ("समझ नहीं आ रहा?") · Action Layer (आज के ३ ज़रूरी काम) · Voice Khata · Business Memory · Alerts · Profile · Fingerprint lock.
**Languages:** हिंदी · Hinglish · English · भोजपुरी · मगही · मैथिली (regional strings flagged for native review).
**Backend:** conversation Brain · deterministic finance engine · NumPy ANN forecaster · NumPy GA loan/expansion optimiser · Bhashini ASR/TTS/translation · loan simulation. 10 tests.

```
paisaflow/
├── android/                         Kotlin · Jetpack Compose · Material 3
│   ├── settings.gradle.kts  build.gradle.kts  gradle/libs.versions.toml
│   └── app/
│       ├── build.gradle.kts         API_BASE_URL is set here
│       └── src/main/
│           ├── AndroidManifest.xml
│           ├── res/                 strings · themes · adaptive launcher icon
│           └── java/com/paisaflow/app/
│               ├── MainActivity.kt
│               ├── PaisaFlowApp.kt              Scaffold: top bar + tab + bottom nav + snackbar
│               ├── ui/theme/                    Color · Type · Spacing · Theme
│               ├── core/model/Models.kt         EvidenceLabel · ExtractedFact · ClarificationQuestion · BrainUnderstanding · VoiceState
│               ├── core/mock/DemoData.kt        SAMPLE dairy business (labelled as sample data)
│               ├── core/network/ApiClient.kt    Retrofit + kotlinx-serialization, sample-data fallback
│               ├── shared/                      AppTopBar · AppBottomNav · PillButton · EvidenceChip · ComingSoon
│               └── feature/
│                   ├── voice/                   VoiceViewModel · VoiceConversationScreen · components/
│                   └── twin/ simulate/ actions/ placeholder tabs
├── backend/                         Python · FastAPI
│   ├── app/main.py  core.py  api/v1/  schemas/conversation.py  services/brain.py
│   ├── requirements.txt  tests_smoke.py  Dockerfile  .env.example
├── docker-compose.yml
└── README.md
```

## 1. Run the backend

```bash
cd backend
python -m venv .venv && source .venv/bin/activate      # Windows: .venv\Scripts\activate
pip install -r requirements.txt
cp .env.example .env
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
# Swagger UI: http://localhost:8000/docs   ·   python tests_smoke.py
```

## 2. Open the Android app

1. Android Studio (Koala or newer) → **Open** → select the `android/` folder.
2. Let Gradle sync (Gradle 8.7 wrapper, AGP 8.5.2, Kotlin 2.0.20, JDK 17). The wrapper JAR is fetched by Android Studio on first sync; if it complains, run `gradle wrapper` once or use *File → Sync Project*.
3. Run on an emulator (API 26+) or a phone.

Backend URL: `android/app/build.gradle.kts` → `API_BASE_URL`.
- Emulator: `http://10.0.2.2:8000/` (default)
- Physical phone on the same Wi-Fi: `http://<your-laptop-LAN-IP>:8000/`
- If the backend is unreachable, the screen renders from sample data and shows a "Sample data" notice.

`usesCleartextTraffic="true"` in the manifest is for the local HTTP dev server only — remove it and use HTTPS for any real deployment.

## Design system (from the reference mockup)

| Token | Value |
| --- | --- |
| Primary / Primary container | `#003629` / `#1B4D3E` |
| Primary fixed / dim | `#BAEED9` / `#9ED1BD` |
| Secondary (accent) | `#9A442D` · fixed `#FFDBD2` |
| Surface / background | `#FBF9F6` |
| Surface containers | lowest `#FFFFFF` · low `#F5F3F0` · high `#EAE8E5` · highest `#E4E2DF` |
| On surface / variant | `#1B1C1A` / `#404945` |
| Outline | `#707974` |
| Type | System sans-serif at the mockup's sizes (drop Plus Jakarta Sans / Inter into `res/font/` and point `Type.kt` at them) |
| Radius | 16dp inner · 32dp cards · pill |
| Touch target | ≥ 56dp |

## Rules this code follows

- The Brain never does arithmetic; finance lives in a deterministic engine (`backend/app/finance/`, next phase).
- Every important value carries an evidence label (FACT / OBSERVATION / ESTIMATE / ASSUMPTION / AI INFERENCE).
- One question at a time; every captured value is read back; "Badalna hai" and "Samajh nahi aaya?" on every step.
- Demo numbers are fictional sample data and are labelled as such in the UI.
- Raw technical errors are never shown to users.

## Next slices (in order)

1. Splash → Language → Onboarding screens
2. Home dashboard + Business Digital Twin card
3. What-If Simulator + Scenario Results (deterministic finance engine + tests)
4. Top 3 Actions, Business Memory timeline
5. Android `SpeechRecognizer` + `TextToSpeech`, PostgreSQL persistence
