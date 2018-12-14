using System;
using System.Collections.Generic;
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
                ListSubject.Where(item => (string.Equals(Major, "Major") ? true : item.Major.EditText.Contains(Major))));

            if (string.Equals(SortText, "Ascending"))
            {
                QuickSortNonRecursive(FilteredSubjects, 0, FilteredSubjects.Count - 1, (i, j) => i < j);
            }
            else if (string.Equals(SortText, "Descending"))
            {
                QuickSortNonRecursive(FilteredSubjects, 0, FilteredSubjects.Count - 1, (i, j) => i > j);
            }


            // Set last filter
            mLastMajor = Major;
            mLastSort = SortText;

            return Task.FromResult(true);
        }

        /// <summary>
        /// Sắp xếp các môn học theo số lượng đăng ký bằng thuật toán quick sort khử đệ quy
        /// </summary>
        /// <param name="source"> Dãy cần sắp xếp </param>
        /// <param name="firstIndex">Vị trí bắt đầu</param>
        /// <param name="lastIndex">Vị trí kết thúc</param>
        /// <param name="Operator">Toán tử sắp xếp</param>
        public void QuickSortNonRecursive(ObservableCollection<SubjectItemViewModel> source, int firstIndex, int lastIndex, Func<int, int, bool> Operator)
        {
            Stack<int> first = new Stack<int>();
            Stack<int> last = new Stack<int>();
            first.Push(firstIndex);
            last.Push(lastIndex);

            while (first.Count != 0)
            {
                int low = first.Pop();
                int high = last.Pop();
                int i = low;
                int j = high;
                SubjectItemViewModel key = source[(i + j) / 2];

                while (i <= j)
                {
                    while (Operator(source[i].TotalStudent, key.TotalStudent)) 
                    {
                        ++i;
                    }

                    while (Operator(key.TotalStudent, source[j].TotalStudent))
                    {
                        --j;
                    }

                    if (i <= j)
                    {
                        SubjectItemViewModel temp = source[i];
                        source[i] = source[j];
                        source[j] = temp;
                        ++i;
                        --j;
                    }
                }

                if (i < high)
                {
                    first.Push(i);
                    last.Push(high);
                }

                if (low < j)
                {
                    first.Push(low);
                    last.Push(j);
                }
            }
        }

        #endregion
    }
}
