import React, { useState } from "react";
import axios from "axios";

function ResumeUpload() {
  const [resumeFile, setResumeFile] = useState(null);
  const [description, setDescription] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!resumeFile || !description.trim()) {
      alert("Please provide both resume file and job description.");
      return;
    }

    const formData = new FormData();
    formData.append("file", resumeFile);
    formData.append("description", description);

    try {
      setLoading(true);
      const response = await axios.post("http://localhost:8080/api/analyze", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      setResult(response.data);
    } catch (error) {
      console.error("Error:", error);
      alert("Something went wrong. Please check the backend.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.title}>🎯 Resume Matcher</h2>
      <form onSubmit={handleSubmit} style={styles.form}>
        <div style={styles.field}>
          <label style={styles.label}>Upload Resume:</label>
          <input
            type="file"
            accept=".pdf,.doc,.docx"
            onChange={(e) => setResumeFile(e.target.files[0])}
          />
        </div>

        <div style={styles.field}>
          <label style={styles.label}>Job Description:</label>
          <textarea
            rows="5"
            style={styles.textarea}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Paste job description here..."
          />
        </div>

        <button type="submit" style={styles.button} disabled={loading}>
          {loading ? "Matching..." : "Match Resume"}
        </button>
      </form>

      {result && (
        <div style={styles.result}>
          <h3>✅ Match Score</h3>
          <div style={styles.progressBar}>
            <div
              style={{
                ...styles.progressFill,
                width: `${result.matchScore}%`,
                backgroundColor: result.matchScore > 70 ? "#4caf50" : "#ff9800",
              }}
            >
              {result.score}%
            </div>
          </div>

          <div style={styles.skillsBlock}>
            <h4>✔️ Matched Skills:</h4>
            <ul>
              {result.matchedSkills.map((skill, idx) => (
                <li key={idx}>{skill}</li>
              ))}
            </ul>
          </div>

          <div style={styles.skillsBlock}>
            <h4>❌ Missing Skills:</h4>
            <ul>
              {result.missingSkills.map((skill, idx) => (
                <li key={idx}>{skill}</li>
              ))}
            </ul>
          </div>
        </div>
      )}
    </div>
  );
}

const styles = {
  container: {
    maxWidth: "700px",
    margin: "40px auto",
    padding: "30px",
    background: "#f9f9f9",
    borderRadius: "12px",
    boxShadow: "0 0 15px rgba(0,0,0,0.1)",
    fontFamily: "Arial, sans-serif",
  },
  title: {
    textAlign: "center",
    marginBottom: "20px",
    color: "#333",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "15px",
  },
  field: {
    display: "flex",
    flexDirection: "column",
  },
  label: {
    marginBottom: "5px",
    fontWeight: "bold",
  },
  textarea: {
    resize: "vertical",
    padding: "10px",
    borderRadius: "6px",
    border: "1px solid #ccc",
    fontSize: "14px",
  },
  button: {
    padding: "12px",
    backgroundColor: "#1976d2",
    color: "white",
    fontSize: "16px",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
  },
  result: {
    marginTop: "30px",
  },
  progressBar: {
    width: "100%",
    backgroundColor: "#ddd",
    borderRadius: "10px",
    overflow: "hidden",
    marginBottom: "20px",
  },
  progressFill: {
    height: "30px",
    lineHeight: "30px",
    color: "white",
    textAlign: "center",
    transition: "width 0.5s ease",
  },
  skillsBlock: {
    marginBottom: "15px",
  },
};

export default ResumeUpload;
