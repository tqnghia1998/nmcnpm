namespace DbModel
{
    /// <summary>
    /// Thông tin tóm gọn bao gồm: Tên môn học, tên khoa, số lượng học sinh đã đăng ký
    /// </summary>
    public class StatisticSubjectItemDTO
    {
        /// <summary>
        /// Tên khoa
        /// </summary>
        public string Major { get; set; }

        /// <summary>
        /// Mã học phần
        /// </summary>
        public string Id { get; set; }

        /// <summary>
        /// Tên học phần
        /// </summary>
        public string Subject { get; set; }

        /// <summary>
        /// Số lượng sinh viên đăng ký
        /// </summary>
        public int TotalStudent { get; set; }
    }
}
