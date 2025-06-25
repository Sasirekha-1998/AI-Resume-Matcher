import React, { useState } from 'react';
import axios from 'axios';

function ResumeUpload() {
  const [file, setFile] = useState(null);
  const [jobDescription, setJobDescription] = useState('');
  const [matchResult, setMatchResult] = useState(null);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleJobDescriptionChange = (e) => {
    setJobDescription(e.target.value);
  };

  const handleAnalyze = async () => {
    if (!file || !jobDescription) {
      alert('Please upload a resume and enter a job description.');
      return;
    }

    const formData = new FormData();
    formData.append('file', file);
    formData.append('description', jobDescription);

    try {
      const response = await axios.post('http://localhost:8080/api/analyze', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      setMatchResult(response.data);
    } catch (error) {
      alert('Analysis failed.');
      console.error(error);
    }
  };

  return (
    <div style={{ padding: '20px' }}>
      <h2>Resume Matcher</h2>

      <input type="file" accept=".pdf" onChange={handleFileChange} />
      <br /><br />

      <textarea
        rows="6"
        cols="50"
        placeholder="Paste job description here..."
        value={jobDescription}
        onChange={handleJobDescriptionChange}
      />
      <br /><br />

      <button onClick={handleAnalyze}>Match Resume</button>

      {matchResult && (
        <div style={{ marginTop: '20px', border: '1px solid #ccc', padding: '10px' }}>
          <h3>Match Results:</h3>
          <p><strong>Match Score:</strong> {matchResult.score}%</p>
          <p><strong>Matched Skills:</strong> {matchResult.matchedSkills.join(', ')}</p>
          <p><strong>Missing Skills:</strong> {matchResult.missingSkills.join(', ')}</p>
        </div>
      )}
    </div>
  );
}

export default ResumeUpload;
