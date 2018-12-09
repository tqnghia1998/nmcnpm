using System.Windows;

namespace DbModel
{
    /// <summary>
    /// Extension method for the <see cref="WebRequestresponseExtensions{T}"/> class
    /// </summary>
    public static class WebRequestresponseExtensions
    {
        /// <summary>
        /// Check the web request result for any errors, displaying them if there any
        /// </summary>
        /// <typeparam name="T">The type of API response</typeparam>
        /// <param name="response">The response to check</param>
        /// <param name="title">The title of the error dialog if there is an error</param>
        /// <returns>Return true if there was an error, or false if all was OK</returns>
        public static bool DisplayErrorIfFailed<T>(this WebRequestResult<ApiResponse<T>> response, string title)
        {
            // If there was no response, bad data, or a response with a error message...
            if (response == null || response.ServerResponse == null || !response.ServerResponse.Successful)
            {
                // Default error message
                // TODO: localize string
                var message = "Unknown error from server call";

                // If we got a response from the server...
                if (response?.ServerResponse != null)
                {
                    // Set message to servers response
                    message = response.ServerResponse.ErrorMessage;
                }
                // If we have a response but deserialize failed...
                else if (!string.IsNullOrWhiteSpace(response?.RawServerResponse))
                {
                    // Set error message
                    message = $"Unexpected response from server. {response.RawServerResponse}";
                }
                // If we have a response but no server response details at all...
                else if (response != null)
                {
                    // Set message to standard HTTP server response details
                    message = $"Failed to communicate with server. Status code {(int)response.StatusCode}. {response.StatusDescription}";
                }

                // Display error
                MessageBox.Show(message, title, MessageBoxButton.OK, MessageBoxImage.Error);

                // Return that we have an error
                return true;
            }

            // All was OK, so return false for no error
            return false;
        }
    }
}
