function z = newtonWolfeBacktrack(objective, initZ)
    % newtonWolfeBacktrack solves an unconstrained optimization problem
    %    using Newton’s method with backtracking and the Weak Wolfe
    %    condition for backtracking termination.
    %
    % z = newtonWolfeBacktrack(objective, initZ)
    %   objective a handle of function that take input z and returns
    %               the values of the objective, the first, and the
    %               second derivative at z
    %   initZ       initial value of z
    %
    ALPHA = 0.05;
    BETA = 0.5;
    TOL = 1e-6;
    MAX_ITER = 100;
    z = initZ;
    for it=1:MAX_ITER
        [val,d,dd] = objective(z);
        % Newton direction
        dz = -d/dd;
        % we are done if derivative is small enough
        if abs(dz) < TOL
            break;
        end
        % backtracking with the Wolfe condition check
        step = 1;
        while objective(z + step*dz) > val + ALPHA*step*dz*d
               step = step*BETA;
        end
        z = z + step*dz;
    end
end
