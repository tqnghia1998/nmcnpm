using DbModel;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Globalization;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace AdminClient
{
    public class ListSubjectViewModel : BaseViewModel
    {
        #region Public Properties

        public ObservableCollection<SubjectItemViewModel> Items { get; set; }

        #endregion

        #region Constructor 

        /// <summary>
        /// DEFAULT CONSTRUCTOR
        /// </summary>
        public ListSubjectViewModel()
        {
            Items = new ObservableCollection<SubjectItemViewModel>();
            Items.Add(new SubjectItemViewModel
            {
                Id = "CSC1",
                Name = "Introduction to Software Engineering",
                DateStart = "0606",
                DateFinish = "0606",
            });

            Items.Add(new SubjectItemViewModel
            {
                Id = "CSC1",
                Name = "Introduction to Software Engineering",
                DateStart = "0606",
                DateFinish = "0606",
            });
        }

        #endregion
    }

}