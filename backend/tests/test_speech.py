import base64

from fastapi.testclient import TestClient

from app.main import app

c = TestClient(app)


def test_sample_mode_without_keys():
    wav = base64.b64encode(b"RIFF....WAVEfmt ").decode()
    r = c.post("/api/v1/speech/transcribe", json={"audio_base64": wav, "language": "bho"})
    assert r.status_code == 200 and r.json()["is_sample_data"] is True
    t = c.post("/api/v1/speech/translate", json={"text": "नमस्ते", "source": "hi", "target": "en"})
    assert t.status_code == 200
    s = c.post("/api/v1/speech/synthesise", json={"text": "नमस्ते सुनीता जी", "language": "hi"})
    assert s.status_code == 200 and s.json()["audio_base64"] is None


def test_bad_audio_rejected():
    r = c.post("/api/v1/speech/transcribe", json={"audio_base64": "not-base64!!", "language": "hi"})
    assert r.status_code == 422
