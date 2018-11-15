using DbModel;

namespace AdminClient
{
    /// <summary>
    /// Store and retrieves information about the client application
    /// </summary>
    public class ClientDataStore
    {
        #region Public Properties

        /// <summary>
        /// The information of the user
        /// </summary>
        public LoginCredentialsDataModel ApplicationUser { get; set; }

        #endregion
    }
}
