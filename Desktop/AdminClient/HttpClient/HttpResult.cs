namespace AdminClient
{
    /// <summary>
    /// Kết quả trả về của web
    /// </summary>
    public class HttpResult
    {
        #region Public Properties

        /// <summary>
        /// Status code http
        /// </summary>
        public int StatusCode { get; set; }

        /// <summary>
        /// Tin nhắn phản hồi
        /// </summary>
        public string MessageResponse { get; set; }

        #endregion

        #region Constructor

        public HttpResult()
        {

        }

        #endregion
    }
}
