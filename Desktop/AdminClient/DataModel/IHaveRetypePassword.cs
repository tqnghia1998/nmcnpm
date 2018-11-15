namespace AdminClient
{
    /// <summary>
    /// An interface for a class can provide a retype secure password
    /// </summary>
    public interface IHaveRetypePassword
    {
        /// <summary>
        /// Check retype password match password
        /// </summary>
        bool RetypePasswordIsCorrect { get; }
    }
}
