using System.Collections.ObjectModel;
using System.Linq;
using System.Threading.Tasks;

namespace AdminClient
{
    /// <summary>
    /// the view model for <see cref="StatisticPage"/>
    /// </summary>
    public class StatisticViewModel: ListSubjectViewModel
    {
        #region Protected Members

        /// <summary>
        /// The last sort text in this list
        /// </summary>
        protected string mLastSort;

        #endregion

        #region Public Properties

        /// <summary>
        /// The text to sort for in the filter command
        /// </summary>
        public string SortText { get; set; }

        /// <summary>
        /// 
        /// </summary>
        public ObservableCollection<string> ListSort { get; set; }

        #endregion

        #region Constructor

        /// <summary>
        /// Default constructor
        /// </summary>
        public StatisticViewModel()
        {

        }

        #endregion

        #region Command Methods

        public override Task Filter()
        {
            // Make sure we don't re-search the same request
            if (string.Equals(Major, mLastMajor) && string.Equals(SortText, mLastSort))
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
                mLastSort = SortText;

                return Task.FromResult(true);
            }

            FilteredSubjects = new ObservableCollection<SubjectItemViewModel>(
                ListSubject.Where(item => (string.Equals(Major, "Major") ? true : item.Major.EditText.Contains(Major)) &&
                    (string.Equals(SortText, "Sort") ? true : item.Term.Contains(Term))));

            // Set last filter
            mLastMajor = Major;
            mLastSort = SortText;

            return Task.FromResult(true);
        }

        #endregion


    }
}
