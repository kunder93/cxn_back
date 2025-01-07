
package es.org.cxn.backapp.test.unit.services;

class EmailServiceTest {
//
//    /**
//     * Mock sender, not real sending.
//     */
//    @Mock
//    private JavaMailSender mailSender;
//
//    /**
//     * Mock mime messages.
//     */
//    @Mock
//    private MimeMessage mimeMessage;
//
//    /**
//     * Service where inject mocks.
//     */
//    @InjectMocks
//    private DefaultEmailService emailService;
//
//    @BeforeEach
//    public void setUp() {
//        // Initialize mocks
//        MockitoAnnotations.openMocks(this);
//
//        // Configure the mock mailSender to return the mocked MimeMessage
//        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
//
//    }
//
//    @Test
//    void testSendSignUpEmail() throws MessagingException, IOException {
//        // Given
//        var toEmail = "test@example.com";
//        var subject = "Test Subject";
//        var body = "This is a test email.";
//
//        // When
//        emailService.sendSignUpEmail(toEmail, subject, body);
//
//        // Then
//        verify(mailSender, times(1)).createMimeMessage();
//        verify(mimeMessage, times(1)).setFrom(new InternetAddress("principal@xadreznaron.es"));
//        verify(mimeMessage, times(1)).setRecipients(RecipientType.TO, InternetAddress.parse(toEmail));
//        verify(mimeMessage, times(1)).setContent(any(String.class), eq("text/html; charset=utf-8"));
//        verify(mailSender, times(1)).send(mimeMessage);
//    }

}
