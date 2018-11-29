using DbModel;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Windows.Input;

namespace AdminClient
{
    public class ListSubjectViewModel : BaseViewModel
    {
        #region Protected Members

        /// <summary>
        /// The last search text in this list
        /// </summary>
        protected string mLastSearchText;

        /// <summary>
        /// The text to search for in the search command
        /// </summary>
        protected string mSearchText;

        /// <summary>
        /// The chat thread items for the list
        /// </summary>
        protected ObservableCollection<SubjectItem> mItems;

        /// <summary>
        /// A flag indicating if the search dialog is open
        /// </summary>
        protected bool mSearchIsOpen = false;

        #endregion

        #region Public Properties

        /// <summary>
        /// List major
        /// </summary>
        public ObservableCollection<string> ListMajor { get; set; }

        /// <summary>
        /// List term
        /// </summary>
        public ObservableCollection<string> ListTerm { get; set; }

        /// <summary>
        /// List subject
        /// </summary>
        public ObservableCollection<string> ListCourse { get; set; }
        
        /// <summary>
        /// List course
        /// </summary>
        public ObservableCollection<string> ListSubjectName { get; set; }

        /// <summary>
        /// The text to search for when we do a search
        /// </summary>
        public string SearchText
        {
            get => mSearchText;
            set
            {
                // Check value is different
                if (mSearchText == value)
                {
                    return;
                }

                // Update value
                mSearchText = value;

                // If the search text is empty...
                if (string.IsNullOrEmpty(SearchText))
                {
                    // Search to restore message
                    Search();
                }
            }
        }

        /// <summary>
        /// A flag indicating if the search dialog is open
        /// </summary>
        public bool SearchIsOpen
        {
            get => mSearchIsOpen;
            set
            {
                // Check value has changed
                if (mSearchIsOpen == value)
                {
                    return;
                }

                // Update value
                mSearchIsOpen = value;

                // If dialog closes...
                if (!mSearchIsOpen)
                {
                    // Clear search text
                    SearchText = string.Empty;
                }
            }
        }

        #endregion

        #region Public Commands

        /// <summary>
        /// The command for when the user wants to search
        /// </summary>
        public ICommand SearchCommand { get; set; }

        /// <summary>
        /// The command for when the user wants to open the search dialog
        /// </summary>
        public ICommand OpenSearchCommand { get; set; }

        /// <summary>
        /// The command for when the user wants to close the search dialog
        /// </summary>
        public ICommand CloseSearchCommand { get; set; }

        /// <summary>
        /// The command for when the user wants to clear the search text
        /// </summary>
        public ICommand ClearSearchCommand { get; set; }

        #endregion

        #region Constructor 

        /// <summary>
        /// DEFAULT CONSTRUCTOR
        /// </summary>
        public ListSubjectViewModel()
        {
            ListMajor = new ObservableCollection<string> { "Major" };
            ListTerm = new ObservableCollection<string> { "Term" };
            ListCourse = new ObservableCollection<string> { "Course" };
            ListSubjectName = new ObservableCollection<string> { "Subject" };

            SearchCommand = new RelayCommand(Search);
            OpenSearchCommand = new RelayCommand(OpenSearch);
            CloseSearchCommand = new RelayCommand(CloseSearch);
            ClearSearchCommand = new RelayCommand(ClearSearch);
        }

        #endregion

        #region Command Methods

        public void LoadInformation(List<string> list)
        {

        }

        /// <summary>
        /// Search the current message list and filters the view
        /// </summary>
        public void Search()
        {
            
        }

        /// <summary>
        /// Clears the search text
        /// </summary>
        public void ClearSearch()
        {
            // If there is some search text...
            if (!(string.IsNullOrEmpty(SearchText)))
            {
                // Clear the text
                SearchText = string.Empty;
            }
            // Otherwise...
            else
            {
                // Close search dialog
                SearchIsOpen = false;
            }
        }

        /// <summary>
        /// Opens search dialog
        /// </summary>
        public void OpenSearch() => SearchIsOpen = true;

        /// <summary>
        /// Closes search dialog
        /// </summary>
        public void CloseSearch() => SearchIsOpen = false;


        #endregion
    }

}