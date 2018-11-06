using System.Security;

namespace AdminClient
{
    /// <summary>
    /// An interface for a class can provide a secure password
    /// </summary>
    public interface IHavePassword
    {
        /// <summary>
        /// The secure password
        /// </summary>
        SecureString SecurePassword { get; }
    }
}
