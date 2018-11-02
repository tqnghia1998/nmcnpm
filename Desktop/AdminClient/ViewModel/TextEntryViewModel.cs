namespace AdminClient
{
    /// <summary>
    /// View model cho text entry control
    /// </summary>
    public class TextEntryViewModel: BaseViewModel
    {
        #region Public Properties

        /// <summary>
        /// Nội dung của label
        /// </summary>
        public string Label { get; set; }

        /// <summary>
        /// Nội dung của edit text
        /// </summary>
        public string EditText { get; set; }

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public TextEntryViewModel()
        {

        }

        #endregion
    }
}
