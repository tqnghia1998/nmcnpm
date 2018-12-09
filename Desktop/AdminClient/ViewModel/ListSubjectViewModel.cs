using DbModel;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;

namespace AdminClient
{
    public class ListSubjectViewModel : BaseViewModel
    {
        #region Private Member

        /// <summary>
        /// Index item that was selected in Major combobox 
        /// </summary>
        private string mLastMajor;

        /// <summary>
        /// Index item that was selected in Term combobox 
        /// </summary>
        private string mLastTerm;

        /// <summary>
        /// Index item that was selected in Course combobox 
        /// </summary>
        private string mLastCourse;

        #endregion

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
        protected ObservableCollection<SubjectItemViewModel> mListSubject;

        /// <summary>
        /// 
        /// </summary>
        protected ObservableCollection<SubjectItemViewModel> mListBeforeFilter;

        /// <summary>
        /// A flag indicating if the search dialog is open
        /// </summary>
        protected bool mSearchIsOpen = false;

        #endregion

        #region Public Properties

        public bool FilterIsRunning { get; set; }

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
        public ObservableCollection<SubjectItemViewModel> ListSubject
        {
            get => mListSubject;

            set
            {
                // Make sure we have not the same list
                if (mListSubject == value)
                {
                    return;
                }

                mListSubject = value;
                FilteredSubjects = mListSubject;
            }
        }

        /// <summary>
        /// The subject thread items for the list subject that include any search filtering
        /// </summary>
        public ObservableCollection<SubjectItemViewModel> FilteredSubjects { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string Major { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string Term { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public string Course { get; set; }

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
        /// The command for when the user wants to filter
        /// </summary>
        public ICommand FilterCommand { get; set; }

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
            SearchCommand = new RelayCommand(Search);
            OpenSearchCommand = new RelayCommand(OpenSearch);
            CloseSearchCommand = new RelayCommand(CloseSearch);
            ClearSearchCommand = new RelayCommand(ClearSearch);
            FilterCommand = new RelayCommand(async () => await FilterAsync());
        }

        #endregion

        #region Command Methods

        /// <summary>
        /// Load any needed information for list subject page
        /// </summary>
        /// <param name="list"></param>
        public void LoadInformation(List<string> list)
        {

        }

        /// <summary>
        /// Search the current message list and filters the view
        /// </summary>
        public void Search()
        {
            // Make sure we don't re-search the same text
            if ((string.IsNullOrEmpty(mLastSearchText) && string.IsNullOrEmpty(SearchText)) || string.Equals(mLastSearchText, SearchText))
            {
                return;
            }

            // If we have no search text or no items
            if (string.IsNullOrEmpty(SearchText) || ListSubject == null || ListSubject.Count == 0)
            {
                // Make filtered list the same 
                FilteredSubjects = ListSubject;

                // Set last search text
                mLastSearchText = SearchText;

                return;
            }

            // Find all subject that contain the given text
            // TODO: Make more efficient search
            FilteredSubjects = new ObservableCollection<SubjectItemViewModel>(
                ListSubject.Where(item => item.Subject.EditText.ToLower().Contains(SearchText.ToLower()) || 
                item.Teacher.EditText.ToLower().Contains(SearchText.ToLower())));

            // Set last search text
            mLastSearchText = SearchText;
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

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public async Task FilterAsync()
        {
            await RunCommand(() => this.FilterIsRunning, async () =>
               {
                   await Task.Run(Filter);
               });
        }

        /// <summary>
        /// As name function
        /// </summary>
        public Task Filter()
        {
            // Make sure we don't re-search the same request
            if (string.Equals(Major, mLastMajor) && string.Equals(Term, mLastTerm) && string.Equals(Course, mLastCourse))
            {
                return Task.FromResult(true);
            }

            // If we have no items
            if (ListSubject == null || ListSubject.Count == 0)
            {
                // Make filtered list the same 
                FilteredSubjects = ListSubject;

                // Set last filter
                mLastMajor = Major;
                mLastTerm = Term;
                mLastCourse = Course;

                return Task.FromResult(true);
            }


            FilteredSubjects = new ObservableCollection<SubjectItemViewModel>(
                ListSubject.Where(item => (string.Equals(Major, "Major") ? true : item.Major.EditText.Contains(Major)) &&
                    (string.Equals(Term, "Term") ? true : item.Term.Contains(Term)) &&
                    (string.Equals(Course, "Course") ? true : item.Course.Contains(Course))));

            // Set last filter
            mLastMajor = Major;
            mLastTerm = Term;
            mLastCourse = Course;

            return Task.FromResult(true);
        }

        #endregion
    }

}