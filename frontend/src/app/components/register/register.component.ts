import {Component} from '@angular/core';
import {UntypedFormBuilder, UntypedFormGroup, Validators} from '@angular/forms';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {RegisterRequest} from '../../dtos/authentication/user-registration';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerForm: UntypedFormGroup;
  submitted = false;
  // Error flag
  error = false;
  email = '';


  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private notification: ToastrService,
    private formBuilder: UntypedFormBuilder,
    private authService: AuthService,
  ) {
    this.registerForm = this.formBuilder.group({
      email: ['',
        [
          Validators.required,
          Validators.pattern(/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/)
        ]
      ],

      firstName: ['',
        [
          Validators.required,
          Validators.pattern(/^[A-Za-z]+$/)
        ]
      ],

      lastName: ['',
        [
          Validators.required,
          Validators.pattern(/^[A-Za-z]+$/)
        ]
      ],

      password: ['',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d\W]{8,}$/)
        ]
      ],
    });
  }

  createUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      const userRegistration: RegisterRequest = new RegisterRequest(
        this.registerForm.controls.email.value,
        this.registerForm.controls.firstName.value,
        this.registerForm.controls.lastName.value,
        this.registerForm.controls.password.value
      );

      this.authService.createUser(userRegistration).subscribe({
        next: () => {
          this.notification.success('Registration successful');
          this.router.navigate(['/login']);
        }, error: error => {
          this.notification.error('Registration failed');
          console.error(JSON.parse(error['error'])['detail']);
          this.error = true;
        }
      });
    } else {
      this.notification.error('Invalid Input');
    }
  }

}
