## Inspiration
We wanted to create a platform that helps reduce waste by connecting people and businesses with nearby reuse options upcycling opportunities. The goal is to make it easy to divert waste from landfills while supporting local communities. We were inspired by the Too Good to Go app for food waste!

## Project Attribution
This repository contains a copy of a collaborative project originally developed with my teammate, Kanishka Agarwal. The original project repository was maintained under her GitHub account, and this copy was created to document my contributions and involvement in the project.

## What it does
Waste Web allows users to:
- Upload details about waste they want to get rid of.
- Receive AI-powered suggestions for reuse and recycling.
- Discover nearby organizations or individuals who can take the waste.
- Track environmental impact (CO2 saved, landfill avoided, etc.).
- Browse a live feed of available waste for reuse in their area (coming soon!)

## How we built it:
Backend: Java with Spring Boot for APIs and services.
Frontend: HTML, CSS
AI Integration: Groq API for waste analysis and suggestions.
Libraries: Lombok for boilerplate reduction, OkHttp for HTTP requests, Jackson for JSON parsing.

## Challenges we ran into:
- Ensuring AI suggestions were meaningful
- Hitting the API limits preventing it from working 
- Managing location-based matches and mock data for demonstration.
- Handling secrets (the API keys) safely without pushing them to GitHub

## Accomplishments that we're proud of:
- Successfully integrating Groq AI to provide real reuse options
- Tracked environmental impact metrics automatically.

## What we learned
- How to securely manage API keys in a project
- What structural front and backends look like

## What's next for Waste Web
- Add authentication for users and businesses
- Enable real-time notifications for nearby matches
- Expand AI suggestions to cover more types of waste.
- Deploy a fully functional web app with a polished frontend.
