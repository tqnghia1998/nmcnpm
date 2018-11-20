namespace AdminClient
{
    public class SubjectItemViewModel
    {
        #region Public Properties

        /// <summary>
        /// Is header of list ?
        /// </summary>
        public bool IsHeader { get; set; }

        /// <summary>
        /// Id Subjects
        /// </summary>
        public string Id { get; set; }

        ///<summary>
        ///Name Subjects
        ///</summary>
        public string Name { get; set; }

        /// <summary>
        /// Term 
        /// </summary>
        public string Term { get; set; }

        /// <summary>
        /// Date start subject
        /// </summary>
        public string DateStart { get; set; }

        /// <summary>
        /// Date finish subject
        /// </summary>
        public string DateFinish { get; set; }
        #endregion
    }
}
