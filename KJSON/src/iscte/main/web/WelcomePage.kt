package iscte.main.web

/**
 * Generates an HTML welcome page for the GetJson Web API.
 *
 * This function creates a simple HTML document that lists the available API endpoints.
 * The HTML includes basic styling for better readability.
 *
 * @param paths A list of strings representing the API endpoints.
 *
 * @return A string containing the HTML document.
 */
fun getWelcomePageHtml(paths: List<String>): String {
    return """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>GetJson Web API</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                line-height: 1.6;
                color: #333;
                max-width: 800px;
                margin: 0 auto;
                padding: 20px;
                background-color: #f5f5f5;
            }
            h1 {
                color: #2c3e50;
                border-bottom: 2px solid #3498db;
                padding-bottom: 10px;
            }
            .api-list {
                background-color: white;
                border-radius: 5px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
                padding: 20px;
                margin-top: 20px;
            }
            .api-item {
                padding: 10px;
                border-bottom: 1px solid #eee;
            }
            .api-item:last-child {
                border-bottom: none;
            }
            .method {
                display: inline-block;
                background-color: #3498db;
                color: white;
                padding: 3px 8px;
                border-radius: 3px;
                font-size: 0.8em;
                margin-right: 10px;
            }
            .path {
                font-family: monospace;
                color: #2980b9;
            }
            .footer {
                margin-top: 30px;
                text-align: center;
                font-size: 0.9em;
                color: #7f8c8d;
            }
        </style>
    </head>
    <body>
        <h1>GetJson Web API</h1>
        <p>Welcome to the GetJson Web API service.</p>
        <p>Below are the valid endpoints. To change them, load a different class into the service:</p>
        
        <div class="api-list">
            ${paths.joinToString("") { path ->
        """
                <div class="api-item">
                    <span class="method">GET</span>
                    <span class="path">${path}</span>
                </div>
                """
    }}
        </div>
        
        <div class="footer">
    Alexandre Cortez & Pedro Moura | ${java.time.LocalDate.now().year}
        </div>
    </body>
    </html>
    """.trimIndent()
}